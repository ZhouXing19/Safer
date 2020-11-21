CodePath Android University Group Project
===

# Safer

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

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

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users can log in to post a danger notice
* All nearby dangers are displayed on a map
* An aggregated list of dangers can be viewed
* Each danger has a detailed page with the respective image or video

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
- Joda Time (https://www.joda.org/joda-time/)

## Preview
<img src="preview_map.gif" height=370><br>

## Environment
- Developed in AndroidStudio 4.0.1
- Tested in Pixel 2 API 30
- A [fake location simulator](https://play.google.com/store/apps/details?id=com.lexa.fakegps&hl=en_US&gl=US) is needed in the AVD

## Progress
### Implemented Functions
- User sign up
- User sign in
- Map interface
- Current Location
- "Submit danger nearby" page
- "Pick a location from map" page (developing)

### TO-DO
- `PickLocationActivity.java`, complete the location acquisition function on click.
- `PostDangerActivity.java`, change the address that user inputs into lat-lang, and save in DB.
