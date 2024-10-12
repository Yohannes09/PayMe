window.onload = function(){
    document.getElementById('registerForm').addEventListener('submit', submitForm)
}

async function submitForm(event) {
    event.preventDefault();  // Prevent the default form submission

    console.log("Form submitted")

    const formData = {
        firstName: document.getElementById('firstname').value,
        lastName: document.getElementById('lastname').value,
        username: document.getElementById('username').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    try {
        const response = await fetch('http://localhost:8080/api/v1/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            alert('User registered successfully');
        } else {
            alert('Failed to register user');
        }
    } catch (error) {
        alert('An error occurred while submitting the form');
        console.error('Error:', error);
    }
}