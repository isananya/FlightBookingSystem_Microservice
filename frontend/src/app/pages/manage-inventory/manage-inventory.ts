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

  flightObj: FlightModel = {
    flightNumber: '',
    sourceAirport: '',
    destinationAirport: '',
    departureTime: '',
    arrivalTime: '',
    duration: '' 
  };

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

  onAddFlight() {
    if (this.flightObj.sourceAirport === this.flightObj.destinationAirport) {
      alert("Source and Destination cannot be the same.");
      return;
    }

    const isoDuration = `PT${this.durationHours}H${this.durationMinutes}M`;
    
    const payload: FlightModel = {
      ...this.flightObj,
      duration: isoDuration,
      departureTime: this.flightObj.departureTime + ":00", 
      arrivalTime: this.flightObj.arrivalTime + ":00"
    };

    this.flightService.addFlight(payload).subscribe({
      next: () => {
        alert('Flight Route Created Successfully!');
        this.flightObj = { flightNumber: '', sourceAirport: '', destinationAirport: '', departureTime: '', arrivalTime: '', duration: '' };
        this.durationHours = 0;
        this.durationMinutes = 0;
      },
      error: () => alert('Failed to create flight. ID might exist.')
    });
  }

  onAddSchedule() {
    this.scheduleObj.availableSeats = this.scheduleObj.totalSeats;

    this.flightService.addSchedule(this.scheduleObj).subscribe({
      next: () => {
        alert('Schedule Created Successfully!');
        this.scheduleObj.departureDate = '';
        this.scheduleObj.basePrice = 0;
        this.scheduleObj.totalSeats = 0;
        this.scheduleObj.availableSeats = 0;
      },
      error: () => alert('Failed to add schedule.')
    });
  }

}
