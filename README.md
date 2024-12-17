# Rokto: A Blood Donation Network

A mobile application designed to connect blood donors with those in need, facilitating life-saving blood donations efficiently.  


## Features  

### Common Features for All Users  
- **Sign Up and Login**  
  - OTP-based phone number verification.  
  - Single account per phone number.  
- **Search for Blood Donors**  
  - Filter by Division, District, and Blood Group.  
  - View eligible donor profiles (based on the 56-day donation rule).  
- **Post Blood Requests**  
  - Post blood requests with contact details and location.  
  - Delete requests once the need is fulfilled.  
- **Dynamic Role Switching**  
  - Normal users can become donors and vice versa.  

### Donor-Specific Features  
- **View Blood Requests**  
  - Access blood requests in their area matching their blood group.  
- **Profile Management**  
  - Update location and last donation date to maintain accurate availability.  


## Technologies Used

- **Languages**: Java
- **Libraries**:
  - **Firebase Authentication** (for user authentication)
  - **Firebase Firestore** (for storing user data, posts, and donor information)
  - **Retrofit** (for network calls)
  - **Glide** (for image loading)
