package org.me.myandroidstuff;

	public class CarPark {

		public String getCarParkIdentity() {
			return carParkIdentity;
		}
		public void setCarParkIdentity(String carParkIdentity) {
			this.carParkIdentity = carParkIdentity;
		}
		public int getCarParkOccupancy() {
			return carParkOccupancy;
		}
		public void setCarParkOccupancy(int carParkOccupancy) {
			this.carParkOccupancy = carParkOccupancy;
		}
		public String getCarParkStatus() {
			return carParkStatus;
		}
		public void setCarParkStatus(String carParkStatus) {
			this.carParkStatus = carParkStatus;
		}
		public int getOccupiedSpaces() {
			return occupiedSpaces;
		}
		public void setOccupiedSpaces(int occupiedSpaces) {
			this.occupiedSpaces = occupiedSpaces;
		}
		public int getTotalCapacity() {
			return totalCapacity;
		}
		public void setTotalCapacity(int totalCapacity) {
			this.totalCapacity = totalCapacity;
		}
		private String carParkIdentity;
		private int carParkOccupancy;
		private String carParkStatus;
		private int occupiedSpaces;
		private int totalCapacity;
	}
