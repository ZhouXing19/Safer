CodePath Android University Group Project
===

# Safer

## Table of Contents
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Wireframes](#Wireframes)

## Overview
### Description
An android navigation app where users can share and view dangers nearby in Hyde Park Region in Chicago, IL.

### App Evaluation
- **Category:** Safety
- **Mobile:** This application will be a mobile solution.
- **Story:** Students and local residents are now aware of crimes or dangers nearby. They have more control over their own safety.
- **Market:** This app will be primarily targeted towards students that attend college.
- **Habit:** Students will most likely use this app at night if they are walking alone, or during the day when they are notified of a nearby danger.
- **Scope:** This project will enable students to see nearby dangers and be able to alert others of dangers they have noticed.

## Unit 10: Milestone Deliverables
1. [X] Updated status of issues in Project board (2pts)
2. [X] Sprint planned for next week (Issues created, assigned & added to project board) (3pts)
3. [X] Completed user stories checked-off in README (2pts)
4. [X] Gifs created to show build progress and added to README (3pts)

### Updated status of issues
<img src="gifs/unit10/status_issues.png" width=400>

### Sprint planned for next week
<img src="gifs/unit10/planned_tasks.png" width=400>

### Lynn
- RV Clickable cell + pull to refresh
<img src="gifs/unit10/Lynn.gif" width=300>

### Molly
- Retrieve real time data from firebase
- Display danger icon and floating window
<img src="gifs/unit10/Molly.png" width=800>

### Zhou
- Updated database schema: added entries for title, category and LatLng
<img src="gifs/unit10/zhou.gif" width=800>

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [X] Users can log in to post a danger notice
- [ ] All nearby dangers are displayed on a map
- [ ] An aggregated list of dangers can be viewed
- [X] Each danger has a detailed page with the respective image or video

**Optional Nice-to-have Stories**

* Navigation to safe locations such as police stations, hospitals, or other shelters

### 2. Screen Archetypes

* Login Page
   * Map with pinned danger locations
   * Filter/Toggle danger types
* Main Activity Page/Map
   * Map with pinned danger locations
   * Filter/Toggle danger types
* Danger Posting Page
   * Address autocompletion
   * Get address by pin on map
   * Latitude/Longitude information
   * Post Pictures and Videos
   * Post with categories
* Danger Viewing Page
   * Recycler view, with each row showing address, time, description
* Danger Detail Viewing Page
   * Imageview / videoview
   * Description view
   * Comment view
   * Comment input

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Map
* Post Danger
* Nearby Dangers

**Flow Navigation** (Screen to Screen)

* Welcome Page
   * Main Map Page
* Main Map Page
   * Nearby Danger Page
   * Post Danger Page (logged in)
   * Login Page/Register Page
* Nearby Danger Page
   * Danger Details Page
* Danger Details Page
   * Edit Danger Page
* Post Danger Page
   * Danger Details Page

## Wireframes
<img src="design/paper_mockup.png" width=800>

### [BONUS] Digital Wireframes & Mockups
[Digital Wireframe] https://github.com/ZhouXing19/Safer/tree/master/design/digital_wireframe.pdf
### [BONUS] Interactive Prototype
<img src="design/mockup_walkthrough.gif" width=200>

## Technology
- Java
- Google Map API
- Firebase
- Adobe XD for UI design
- Joda Time

## Environment
- Developed in AndroidStudio 4.0.1
- Tested in Pixel 2 API 30
- A [fake location simulator](https://play.google.com/store/apps/details?id=com.lexa.fakegps&hl=en_US&gl=US) is needed in the AVD

