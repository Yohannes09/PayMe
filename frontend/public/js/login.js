// Main Function - Initializes Event Listeners
window.onload = function () {
    initializeLoginForm();
};

// Initializes the Login Form
function initializeLoginForm() {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleFormSubmission);
    } else {
        console.error("Login form not found.");
    }
}

// Handles the Form Submission
async function handleFormSubmission(event) {
    event.preventDefault(); // Prevent default form submission behavior

    const formData = getFormData();
    if (!isFormDataValid(formData)) {
        alert('Please enter your username and password.');
        return;
    }

    try {
        const responseData = await authenticateUser(formData);
        handleSuccessfulLogin(responseData);
    } catch (error) {
        handleFailedLogin(error);
    }
}

// Extract Form Data
function getFormData() {
    return {
        usernameOrEmail: document.getElementById('usernameOrEmail')?.value,
        password: document.getElementById('password')?.value
    };
}

// Validates Form Data
function isFormDataValid(formData) {
    return formData.usernameOrEmail && formData.password;
}

// Sends Login Request to the Server
async function authenticateUser(formData) {
    const API_URL = 'http://localhost:8080/api/v1/auth/login';

    const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    });

    if (!response.ok) {
        throw new Error('Login request failed.');
    }

    return response.json(); // Parses the JSON response
}

function handleSuccessfulLogin(responseData) {
    console.log('Response data from server:', responseData);

    // Save token and userId to localStorage
    localStorage.setItem('token', responseData.token);
    localStorage.setItem('userId', responseData.userId);

    // Redirect the user to the dashboard dynamically
    const baseUrl = window.location.origin; // E.g., http://localhost:3000
    window.location.href = `${baseUrl}/dashboard/dashboard.html`;

}

function handleFailedLogin(error) {
    alert('Failed to authenticate user.');
    console.error('Error during login:', error);
}