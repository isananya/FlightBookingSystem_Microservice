export interface FlightModel {
  flightNumber: string;
  sourceAirport: string;
  destinationAirport: string;
  departureTime: string; // HH:mm:ss
  arrivalTime: string;   // HH:mm:ss
  duration: string;      // e.g., "PT4H"
}

export interface ScheduleRequestDTO {
  airlineName: string;
  departureDate: string; // YYYY-MM-DD
  basePrice: number;
  totalSeats: number;
  availableSeats: number;
  flightNumber: string;
}