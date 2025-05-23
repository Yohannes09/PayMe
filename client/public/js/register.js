const registerEndpoint = 'http://localhost:8080/api/v1/auth/register';

window.onload = function(){
    document.getElementById('registerForm').addEventListener('submit', submitForm)
}

async function submitForm(event) {
    event.preventDefault();  // Prevent the default form submission

    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if(!isPasswordMatch(password, confirmPassword)){
        alert('Passwords do not match')
        return;
    }

    const requestData = formData();

    try {
        const response = await fetch(registerEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestData)
        });

        if (response.ok) {
            alert('User registered successfully');
            window.location.href = '/authentication/login.html';
        } else {
            alert('Failed to register user');
        }

    } catch (error) {
        alert('An error occurred while submitting the form');
        console.error('Error:', error);
    }
}

function isPasswordMatch(password, confirmPassword){
    return password === confirmPassword;
}

function formData(){
    return {
       firstName: document.getElementById('firstname').value,
       lastName: document.getElementById('lastname').value,
       username: document.getElementById('username').value,
       email: document.getElementById('email').value,
       password: document.getElementById('password').value
   }
}