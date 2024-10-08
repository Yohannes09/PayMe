async function submitForm(event) {
    event.preventDefault();  // Prevent the default form submission

    const formData = {
        firstname: document.getElementById('firstname').value,
        lastname: document.getElementById('lastname').value,
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        confirmPassword: document.getElementById('confirm-password').value
    };

    try {
        const response = await fetch('/ap1/v1/tenmo/register', {
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