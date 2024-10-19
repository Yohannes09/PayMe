window.onload = function(){
    document.getElementById('loginForm').addEventListener('submit', submitForm)
}

async function submitForm(event) {
    event.preventDefault();  // Prevent the default form submission

    console.log("Form submitted")

    const formData = {
        usernameOrEmail: document.getElementById('usernameOrEmail').value,
        password: document.getElementById('password').value
    };

    try {
        const response = await fetch('http://localhost:8080/api/v1/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            const responseData = await response.json();
            alert('User authenticated successfully');
        } else {
            alert('Failed to authenticate user');
        }
    } catch (error) {
        alert('An error occurred while submitting the form');
        console.error('Error:', error);
    }
}