package com.chubb.FlightBookingSystem.model;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(unique = true, nullable = false, length = 6)
	private String pnr;
	
	private boolean roundTrip;
	
	@Column(name="departure_schedule_id", nullable=false)
	private int departureScheduleId;
	
	@Column(name="return_schedule_id",nullable=true)
	private Integer returnScheduleId=null;
	
	private float totalAmount;
	
	@Email
	private String emailId;
	
	@Min(value=1)
	@Column(name = "passengers_count")
	private int passengerCount;

	public Booking(boolean roundTrip, int departureScheduleId, Integer returnScheduleId, float totalAmount,
			@Email String emailId, @Min(1) int passengerCount) {
		super();
		this.pnr = RandomStringUtils.randomAlphanumeric(6);
		this.roundTrip = roundTrip;
		this.departureScheduleId = departureScheduleId;
		this.returnScheduleId = returnScheduleId;
		this.totalAmount = totalAmount;
		this.emailId = emailId;
		this.passengerCount = passengerCount;
	}
}
