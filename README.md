# final_project
Test 1
Add user with API https://bookstore.toolsqa.com/Account/v1/User
Go to https://demoqa.com/login
Login with added account
Go to https://demoqa.com/profile
Click on 'Delete Account'
Validate popup message 'User Deleted.'
Try to login with deleted account credentials
Validate error text 'Invalid username or password!'
Call https://bookstore.toolsqa.com/Account/v1/Authorized with deleted credentials
Validate response message "User not found!"
Test 2
Go to https://demoqa.com/books
Write 'O'Reilly Media' in search textbox
Call https://bookstore.toolsqa.com/BookStore/v1/Books
Validate that count of books with publisher 'O'Reilly Media' is equals to returned list size in UI
Validate that book with title 'Understanding ECMAScript 6' is the last element in UI and in API

Project evaluation criteria
◦ All requirements of Project Body are met - 15 points
◦ Acceptable project structure - Serialization/Deserialization and Page Object Model are met - 5 points
◦ Allure report and Jenkins scheduled job are met - 5 points