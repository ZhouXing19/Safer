# Firebase Tutorial

Last update: Nov 6, Zhou

## Setting up

- In AndroidStudio, click on "Tools" in the top panel, and click on **Firebase**
- Click **Realtime Database**, click on **Save and retrive data**, follow the instructions and finish the **first two steps**

## Write data into database

- Go to the firebase page, click on Realtime Database
- Click on **Rules** and set both parameters `true` to make sure the db is editable.
- Back to **Data** panel, build the elementary db structure
    - Click on "+" and set the name "Danger", leave the Value empty
    - Click on the "+" right of the value editTextView, and set a placeholder with name "demo", value = true

- Back to Android Studio, construct an object class for each `Danger`. 
    - The hierarchy should be : Danger - danger_id - danger_properties
    - In `app\java\com.example.safer`, build a new class `DangerHelperClass`
    ```java
    package com.example.safer;
    
    public class DangerHelperClass {
        String Time, Description, Location;
    
        // Empty constructor
        public DangerHelperClass() {
        }
    
        // Overwritten constructor
        public DangerHelperClass(String time, String description, String location) {
            this.Time = time;
            this.Description = description;
            this.Location = location;
        }
    
        // Getter for all attributes
        public String getTime() {
            return Time;
        }
    
        public String getDescription() {
            return Description;
        }
    
        public String getLocation() {
            return Location;
        }
    }
    ```
 
 - Back to the activity from which you'd like to push your data to db
 - Before `OnCreate`, declare:
   ```java
   FirebaseDatabase rootNode;
   DatabaseReference dangerReference, userReference;
   ```
   

  
- Get a danger_id using by a IdGenerator
    ```java
    Random random = new Random();
    IdGenerator idGenerator = new IdGenerator(random);
    String danger_id = idGenerator.nextId();
    ```
 
- Get current user_id

    ```java
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ```


- Get to the node you would like to write

   ```java
  rootNode = FirebaseDatabase.getInstance();
  dangerReference = rootNode.getReference("Danger");
   ``` 
  
- Generate and write the object
    ```java
    DangerHelperClass dangerClass = new DangerHelperClass(strTime, strDescript, strLocation);
    dangerReference.child(danger_id).setValue(dangerClass);
    ```