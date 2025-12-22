import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../services/flight';
import { FlightModel, ScheduleRequestDTO } from '../../models/inventory';
import airportData from '../../../assets/airports.json';

@Component({
  selector: 'app-manage-inventory',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manage-inventory.html',
  styleUrl: './manage-inventory.css',
})
export class ManageInventory implements OnInit{
  activeTab: 'flight' | 'schedule' = 'flight';

  airports: any[] = airportData; 
  minDate: string = '';
durationHours: number = 0;
  durationMinutes: number = 0;

  // 1. FLIGHT MODEL (Matches Backend)
  flightObj: FlightModel = {
    flightNumber: '',
    sourceAirport: '',
    destinationAirport: '',
    departureTime: '',
    arrivalTime: '',
    duration: '' // Will be calculated
  };

  // 2. SCHEDULE MODEL (Matches Backend)
  scheduleObj: ScheduleRequestDTO = {
    flightNumber: '',
    airlineName: '',
    departureDate: '',
    basePrice: 0,
    totalSeats: 0,
    availableSeats: 0 
  };

  constructor(private flightService: FlightService) {}

  ngOnInit() {
    this.minDate = new Date().toISOString().split('T')[0];
  }

  showFlightForm() { this.activeTab = 'flight'; }
  showScheduleForm() { this.activeTab = 'schedule'; }

  // --- SUBMIT FLIGHT ---
  onAddFlight() {
    if (this.flightObj.sourceAirport === this.flightObj.destinationAirport) {
      alert("Source and Destination cannot be the same.");
      return;
    }

    // 1. Format Duration to ISO-8601 (e.g., PT2H30M)
    const isoDuration = `PT${this.durationHours}H${this.durationMinutes}M`;
    
    // 2. Format Times (Append :00 for seconds if needed)
    const payload: FlightModel = {
      ...this.flightObj,
      duration: isoDuration,
      departureTime: this.flightObj.departureTime + ":00", 
      arrivalTime: this.flightObj.arrivalTime + ":00"
    };

    this.flightService.addFlight(payload).subscribe({
      next: () => {
        alert('Flight Route Created Successfully!');
        // Reset Form
        this.flightObj = { flightNumber: '', sourceAirport: '', destinationAirport: '', departureTime: '', arrivalTime: '', duration: '' };
        this.durationHours = 0;
        this.durationMinutes = 0;
      },
      error: () => alert('Failed to create flight. ID might exist.')
    });
  }

  // --- SUBMIT SCHEDULE ---
  onAddSchedule() {
    // 1. Auto-set available seats to equal total seats
    this.scheduleObj.availableSeats = this.scheduleObj.totalSeats;

    this.flightService.addSchedule(this.scheduleObj).subscribe({
      next: () => {
        alert('Schedule Published Successfully!');
        // Reset specific fields
        this.scheduleObj.departureDate = '';
        this.scheduleObj.basePrice = 0;
        this.scheduleObj.totalSeats = 0;
        this.scheduleObj.availableSeats = 0;
      },
      error: () => alert('Failed to add schedule.')
    });
  }

}
