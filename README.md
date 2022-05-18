# CoolSurveys

This is the project of the course DataBases2 at PoliMi in academic year 2020-2021.

## Functional Specifications
An application deals with gamified consumer data collection. A user registers with a username, a password and an email. A registered user logs in and accesses a HOME PAGE where a “Questionnaire of the day” is published.

The **HOME PAGE** displays the name and the image of the “product of the day” and the product reviews by other users. The **HOME PAGE** comprises a link to access a **QUESTIONNAIRE PAGE** with a questionnaire divided in two sections: a section with a variable number of marketing questions about the product of the day (e.g., Q1: “Do you know the product?” Q2: Have you purchased the product before?” and Q3 “Would you recommend the product to a friend?”) and a section with fixed inputs for collecting statistical data about the user: age, sex, expertise level (low, medium high).

The user fills in the marketing section, then accesses (with a next button) the statistical section where she can complete the questionnaire and submit it (with a submit button), cancel it (with a cancel button), or go back to the previous section and change the answers (with a previous button). All inputs of the marketing section are mandatory. All inputs of the statistical section are optional.
After successfully submitting the questionnaire, the user is routed to a page with a thanks and greetings message.

The database contains a table of offensive words. If any response of the user contains a word listed in the table, the transaction is rolled back, no data are recorded in the database, and the user’s account is blocked so that no questionnaires can be filled in by such account in the future.
When the user submits the questionnaire one or more trigger compute the gamification points to assign to the user for the specific questionnaire, according to the following rule:
1. One point is assigned for every answered question of section 1 (remember that the number of questions can vary in different questionnaires).
2. Two points are assigned for every answered optional question of section 2.

When the user cancels the questionnaire, no responses are stored in the database. However, the database retains the information that the user X has logged in at a given date and time.

The user can access a **LEADERBOARD** page, which shows a list of the usernames and points of all the users who filled in the questionnaire of the day, ordered by the number of points (descending).
The administrator can access a dedicated application on the same database, which features the following pages:
- A **CREATION page** for inserting the product of the day for the current date or for a posterior 
date and for creating a variable number of marketing questions about such product. 
- An **INSPECTION page** for accessing the data of a past questionnaire. The visualized data for a 
given questionnaire include
  * List of users who submitted the questionnaire.
  * List of users who cancelled the questionnaire.
  * Questionnaire answers of each user. 
- A **DELETION page** for ERASING the questionnaire data and the related responses and points
of all users who filled in the questionnaire. Deletion should be possible only for a date 
preceding the current date.

## Technical specification
The application should be realized with the following technologies:
1. MySQL DBMS
2. TomEE JEE application/web server
3. Java servlet and templating (JSP, JSTL or Thymeleaf) for the user interface
4. EJB (stateless or stateful) for the business objects
5. JPA for object relational mapping and transaction management
6. Triggers and constraints for database-level business constraints and rules.

The use of additional technologies is admitted but should be discussed with the teacher.
The use of alternative technologies is not possible.
A technical installation guide and examples of design specifications and coding are published in the Beep portal of the course.

## Evaluation
The evaluation consists of:
1. The discussion of the design presentation 
2. A demo of the application
3. A code walkthrough with questions about the design and the coding. Each member of the group must demonstrate during the individual discussion full knowledge of all the design and coding decisions

## Implementation
In our implementation we used VueJs as an additional technology for client side programming, and JWT to implement token authentication.
These feature and technology were not required, we just implemented them in order to try them ourselves.
