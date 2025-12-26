import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FlightService } from '../../services/flight';
import { BookingRequest, Passenger } from '../../models/booking';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-book',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './book.html',
  styleUrl: './book.css',
})
export class Book implements OnInit {
  @ViewChild('bookForm') bookForm!: NgForm;

  passengerCount: number = 1;
  isRoundTrip: boolean = false;
  depId: number = 0;
  retId: number | null = null;
  email: string = '';
  passengers: Passenger[] = [];
  error = '';
  showSuccessModal: boolean = false;
  pnr: String = '';

  rows = Array.from({ length: 20 }, (_, i) => i + 1);
  colsLeft = ['A', 'B', 'C'];
  colsRight = ['D', 'E', 'F'];
  
  selectedSeatsOutbound: string[] = [];
  selectedSeatsReturn: string[] = [];

  constructor(
    private flightService: FlightService,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.email = this.authService.getEmail();
      this.passengerCount = +params['count'] || 1;
      this.isRoundTrip = params['round'] === 'true';
      this.depId = +params['depId'];
      this.retId = params['retId'] ? +params['retId'] : null;

      this.passengers = Array.from({ length: this.passengerCount }, () => ({
        firstName: '',
        lastName: '',
        age: null,
        gender: '',
        mealOption: '',
        departureSeatNumber: '',
        returnSeatNumber: ''
      }));
    });
  }

  get isSeatSelectionComplete(): boolean {
    const outboundOk = this.selectedSeatsOutbound.length === this.passengerCount;
    const returnOk = !this.isRoundTrip || (this.selectedSeatsReturn.length === this.passengerCount);
    return outboundOk && returnOk;
  }

  nextPassengerIndex(type: 'outbound' | 'return'): number {
    const list = type === 'outbound' ? this.selectedSeatsOutbound : this.selectedSeatsReturn;
    return list.length < this.passengers.length ? list.length : this.passengers.length - 1;
  }

  toggleSeat(seatId: string, type: 'outbound' | 'return') {
    const targetList = type === 'outbound' ? this.selectedSeatsOutbound : this.selectedSeatsReturn;

    if (targetList.includes(seatId)) {
      const index = targetList.indexOf(seatId);
      targetList.splice(index, 1);
      
      if (type === 'outbound') {
        this.passengers[index].departureSeatNumber = '';
      } else {
        this.passengers[index].returnSeatNumber = '';
      }
      return;
    }

    if (targetList.length >= this.passengerCount) {
      alert("All passengers have assigned seats. Deselect one to change.");
      return;
    }

    targetList.push(seatId);
    const pIndex = targetList.length - 1;

    if (type === 'outbound') {
      this.passengers[pIndex].departureSeatNumber = seatId;
    } else {
      this.passengers[pIndex].returnSeatNumber = seatId;
    }
  }

  isSelected(seatId: string, type: 'outbound' | 'return'): boolean {
    const list = type === 'outbound' ? this.selectedSeatsOutbound : this.selectedSeatsReturn;
    return list.includes(seatId);
  }

  confirmBooking() {
    if (!this.isSeatSelectionComplete) {
      alert("Please select all seats before booking.");
      return;
    }

    const request: BookingRequest = {
      emailId: this.email,
      roundTrip: this.isRoundTrip,
      departureScheduleId: this.depId,
      passengerCount: this.passengerCount,
      passengers: this.passengers
    };

    if (this.isRoundTrip && this.retId) {
      request.returnScheduleId = this.retId;
    }

    if (!this.isRoundTrip) {
      request.passengers.forEach(p => delete p.returnSeatNumber);
    }

    this.flightService.bookFlight(request).subscribe({
      next: (response: any) => {
        this.pnr = response;
        this.showSuccessModal = true;
        this.cd.detectChanges();
      },
      error: (err) => {
        if (err.status === 201 || err.status === 200) {
          this.pnr = err.error.text || 'Confirmed';
          this.showSuccessModal = true;
          this.cd.detectChanges();
        } else {
          console.error(err);
          this.error = 'Booking failed';
          alert('Booking failed. Please check your details.');
        }
      }
    });
  }

  closeModal() {
    this.showSuccessModal = false;
    this.router.navigate(['/']);
  }
}