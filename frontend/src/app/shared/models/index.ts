export interface User {
  id: string;
  email: string;
  createdAt: string;
}

export interface Workspace {
  id: string;
  name: string;
  description: string;
  owner: User;
  members: User[];
  createdAt: string;
  updatedAt: string;
  pages?: Page[];
}

export interface Page {
  id: string;
  title: string;
  workspace: Workspace;
  parentPage?: Page;
  childPages?: Page[];
  blocks?: Block[];
  pageOrder: number;
  createdAt: string;
  updatedAt: string;
}

export interface Block {
  id: string;
  page: Page;
  type: 'paragraph' | 'heading1' | 'heading2' | 'heading3' | 'bulletList' | 'numberedList' | 'codeBlock' | 'quote';
  content: string;
  blockOrder: number;
  createdAt: string;
  updatedAt: string;
}
