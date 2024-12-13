# Blood Doner Finder

Welcome to the **Blood Doner Finder** project! 💉

This is an Android application designed to help people find blood donors in their area. The app allows users to sign up either as a **donor** or a **normal user**. The goal is to create a platform where blood donors can connect with people in need of blood by searching for donors based on blood group and location.

## Features

### 1. **User Registration**:
   - **Donors** can sign up to offer their blood for donation.
   - **Normal Users** can register to search for donors and post requests when they need blood.

### 2. **Search Blood Donors**:
   - Users can search for donors by **blood group** and **location**.
   - The app will display a list of available donors based on the search criteria.

### 3. **Post a Blood Request**:
   - If no donor is available at the moment, a user can post a blood request.
   - Donors will be notified of the request and can contact the user who posted it.

### 4. **Donation Tracking**:
   - Donors can track the dates they have donated blood.
   - Donor information will not be displayed for 90 days after donating to allow for a safe recovery period before they can donate again.

### 5. **Normal User to Donor**:
   - Any normal user can choose to become a donor by updating their profile.
   - This allows more flexibility and increases the pool of available donors.

## Technologies Used

- **Languages**: Java, Kotlin
- **Libraries**:
  - **Firebase Authentication** (for user authentication)
  - **Firebase Firestore** (for storing user data, posts, and donor information)
  - **Retrofit** (for network calls)
  - **Glide** (for image loading)
- **Tools**: Android Studio
