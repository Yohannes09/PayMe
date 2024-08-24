// This event listener waits for the HTML content to be fully loaded before running the script
document.addEventListener('DOMContentLoaded', function() {

    // Select the form element by its ID
    const loginForm = document.getElementById('loginForm'); // create an id on whatever html component you're working with

    // Add an event listener to the form for when it is submitted
    loginForm.addEventListener('submit', function(event) {
        // Prevent the default behavior of the form, which would normally submit it
        event.preventDefault();

        // Create an object to hold the form data
        const formData = {
            username: document.getElementById('username').value, // Get the value entered in the username field
            password: document.getElementById('password').value  // Get the value entered in the password field
        };

        // Send an HTTP POST request to the server
        fetch('http://localhost:8080/login', {  // '/login' is the URL where the form data is sent
            method: 'POST', // Specify the HTTP method as POST
            headers: {
                'Content-Type': 'application/json', // Indicate that the data is in JSON format
            },
            body: JSON.stringify(formData) // Convert the form data object to a JSON string
        })
        .then(response => {
            if (response.ok) {
                // If the server response is OK (status code 200), redirect to the success page
                window.location.href = '/success'; // '/success' is the URL of the success page
            } else {
                // If the response is not OK, log an error to the console (for debugging)
                console.error('Login failed.');
            }
        })
        .catch(error => {
            // If there is a network error or other issue, log the error to the console
            console.error('An error occurred during the login process:', error);
        });
    });
});