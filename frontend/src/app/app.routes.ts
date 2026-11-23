import { Routes } from '@angular/router';

import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { ArticleDetail } from './pages/article-detail/article-detail';
import { CreateArticle } from './pages/create-article/create-article';

export const routes: Routes = [
  {
    path: '',
    component: Home
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: 'articles/create',
    component: CreateArticle
  },
  {
    path: 'articles/:slug',
    component: ArticleDetail
  },
  {
    path: '**',
    redirectTo: ''
  }
];