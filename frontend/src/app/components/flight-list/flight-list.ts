import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-flight-list',
  imports: [CommonModule],
  templateUrl: './flight-list.html',
  styleUrl: './flight-list.css',
})
export class FlightList {
  @Input() title = '';
  @Input() flights: any[] | null = [];
  @Input() selectedId: number | null = null;

  @Output() flightSelected = new EventEmitter<number | null>();

  selectFlight(id: number) {
    if (this.selectedId === id) {
      this.flightSelected.emit(null);
    } else {
      this.flightSelected.emit(id);
    }
  }

  formatDuration(duration: string): string {
    const match = duration.match(/PT(?:(\d+)H)?(?:(\d+)M)?/);
    if (!match) return '';

    const hours = match[1] ? `${match[1]}h` : '';
    const minutes = match[2] ? ` ${match[2]}m` : '';

    return `${hours}${minutes}`.trim();
  }

}


