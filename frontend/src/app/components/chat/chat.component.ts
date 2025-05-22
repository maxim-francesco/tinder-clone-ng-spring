import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewChecked,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';
import { MessageService } from '../../services/message.service';
import { MatchService } from '../../services/match.service';
import { MessageDTO } from '../../models/message.model';
import { UserDTO } from '../../models/user.model';
import { MatchDTO } from '../../models/match.model';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  matchId: number = 0;
  match: MatchDTO | null = null;
  matchedUser: UserDTO | null = null;
  messages: MessageDTO[] = [];
  newMessage: string = '';

  isLoadingMatch: boolean = false;
  isLoadingMessages: boolean = false;
  isSendingMessage: boolean = false;
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private messageService: MessageService,
    private matchService: MatchService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.matchId = +params['matchId'];
      this.loadMatchDetails();
    });
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  get currentUser(): UserDTO | null {
    return this.userService.currentUserValue;
  }

  loadMatchDetails(): void {
    if (!this.currentUser?.id || !this.matchId) {
      this.errorMessage = 'Invalid match or user information';
      return;
    }

    this.isLoadingMatch = true;
    this.errorMessage = null;

    // In a real app, you would have an endpoint to get a specific match
    // For now, we'll use findMatches and filter
    this.matchService.findMatches(this.currentUser.id).subscribe({
      next: (matches) => {
        const match = matches.find((m) => m.id === this.matchId);
        if (match) {
          this.match = match;
          this.loadMatchedUserDetails();
          this.loadMessages();
        } else {
          this.isLoadingMatch = false;
          this.errorMessage = 'Match not found';
        }
      },
      error: (error) => {
        this.isLoadingMatch = false;
        this.errorMessage = 'Failed to load match details. Please try again.';
        console.error('Match loading error:', error);
      },
    });
  }

  loadMatchedUserDetails(): void {
    if (!this.match || !this.currentUser?.id) return;

    const otherUserId =
      this.match.user1Id === this.currentUser.id
        ? this.match.user2Id
        : this.match.user1Id;

    this.userService.getUserById(otherUserId).subscribe({
      next: (user) => {
        this.matchedUser = user;
        this.isLoadingMatch = false;
      },
      error: (error) => {
        this.isLoadingMatch = false;
        this.errorMessage = 'Failed to load user details. Please try again.';
        console.error('User loading error:', error);
      },
    });
  }

  loadMessages(): void {
    if (!this.matchId) return;

    this.isLoadingMessages = true;

    this.messageService.getAllByMatch(this.matchId).subscribe({
      next: (messages) => {
        this.messages = messages;
        this.isLoadingMessages = false;
        setTimeout(() => this.scrollToBottom(), 100);
      },
      error: (error) => {
        this.isLoadingMessages = false;
        this.errorMessage = 'Failed to load messages. Please try again.';
        console.error('Messages loading error:', error);
      },
    });
  }

  sendMessage(): void {
    if (
      !this.newMessage.trim() ||
      !this.currentUser?.id ||
      !this.matchedUser?.id ||
      !this.matchId
    ) {
      return;
    }

    this.isSendingMessage = true;

    const messageDto: MessageDTO = {
      userSenderId: this.currentUser.id,
      userReceiverId: this.matchedUser.id,
      matchId: this.matchId,
      content: this.newMessage.trim(),
    };

    this.messageService.sendMessage(messageDto).subscribe({
      next: (message) => {
        this.messages.push(message);
        this.newMessage = '';
        this.isSendingMessage = false;
        setTimeout(() => this.scrollToBottom(), 100);
      },
      error: (error) => {
        this.isSendingMessage = false;
        this.errorMessage = 'Failed to send message. Please try again.';
        console.error('Message sending error:', error);
      },
    });
  }

  isCurrentUserMessage(message: MessageDTO): boolean {
    return message.userSenderId === this.currentUser?.id;
  }

  private scrollToBottom(): void {
    try {
      this.messagesContainer.nativeElement.scrollTop =
        this.messagesContainer.nativeElement.scrollHeight;
    } catch (err) {}
  }

  formatMessageDate(dateTime: string | undefined): string {
    if (!dateTime) return '';

    const date = new Date(dateTime);
    const today = new Date();
    const yesterday = new Date(today);
    yesterday.setDate(yesterday.getDate() - 1);

    if (date.toDateString() === today.toDateString()) {
      return 'Today';
    } else if (date.toDateString() === yesterday.toDateString()) {
      return 'Yesterday';
    } else {
      return date.toLocaleDateString();
    }
  }

  formatMessageTime(dateTime: string | undefined): string {
    if (!dateTime) return '';

    const date = new Date(dateTime);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }
}
