import { Component } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';

interface User {
  id: number;
  name: string;
  age: number;
  image: string;
  bio: string;
}

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [NgFor, CommonModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent {
  // Mock data for UI design purposes only
  users: User[] = [
    {
      id: 1,
      name: 'Emma',
      age: 28,
      image: 'https://randomuser.me/api/portraits/women/1.jpg',
      bio: 'Love hiking and outdoor activities',
    },
    {
      id: 2,
      name: 'James',
      age: 32,
      image: 'https://randomuser.me/api/portraits/men/2.jpg',
      bio: 'Foodie and travel enthusiast',
    },
    {
      id: 3,
      name: 'Sophia',
      age: 26,
      image: 'https://randomuser.me/api/portraits/women/3.jpg',
      bio: 'Artist and coffee lover',
    },
    {
      id: 4,
      name: 'Michael',
      age: 30,
      image: 'https://randomuser.me/api/portraits/men/4.jpg',
      bio: 'Tech geek and fitness freak',
    },
    {
      id: 5,
      name: 'Olivia',
      age: 25,
      image: 'https://randomuser.me/api/portraits/women/5.jpg',
      bio: 'Bookworm and nature lover',
    },
    {
      id: 6,
      name: 'William',
      age: 29,
      image: 'https://randomuser.me/api/portraits/men/6.jpg',
      bio: 'Music producer and dog person',
    },
  ];
}
