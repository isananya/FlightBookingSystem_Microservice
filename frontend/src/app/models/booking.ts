export interface Passenger {
  firstName: string;
  lastName: string;
  age: number | null;
  gender: string;
  mealOption: string;
  departureSeatNumber: string;
  returnSeatNumber?: string;
}

export interface BookingRequest {
  emailId: string;
  roundTrip: boolean;
  departureScheduleId: number;
  returnScheduleId?: number;
  passengerCount: number;
  passengers: Passenger[];
}