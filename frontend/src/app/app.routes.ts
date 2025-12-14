import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Signup } from './components/signup/signup';
import { FlightSearch } from './components/flight-search/flight-search';
import { FlightResults } from './components/flight-results/flight-results';

export const routes: Routes = [
    { path: '', component: FlightSearch },
    { path: 'results', component: FlightResults },
    { path: 'login', component: Login },
    { path: 'signup', component: Signup }
];
