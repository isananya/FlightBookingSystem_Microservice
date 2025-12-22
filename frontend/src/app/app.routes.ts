import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Signup } from './pages/signup/signup';
import { FlightSearch } from './pages/flight-search/flight-search';
import { FlightResults } from './pages/flight-results/flight-results';
import { Book } from './pages/book/book';
import { BookingHistory } from './pages/booking-history/booking-history';
import { ManageInventory } from './pages/manage-inventory/manage-inventory';

export const routes: Routes = [
    { path: '', component: FlightSearch },
    { path: 'results', component: FlightResults },
    { path: 'login', component: Login },
    { path: 'signup', component: Signup },
    { path: 'book', component: Book },
    { path: 'booking-history', component:BookingHistory},
    { path: 'inventory', component:ManageInventory}
];
