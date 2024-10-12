const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const PORT = 3000; // Set the port to 3000

// Middleware
app.use(cors()); // Enable CORS for all requests
app.use(bodyParser.json()); // Parse JSON request bodies

// Sample registration endpoint
app.post('/api/v1/auth/register', (req, res) => {
    const { firstName, lastName, username, email, password } = req.body;

    // You can add your registration logic here
    console.log('User registered:', { firstName, lastName, username, email, password });

    // Send a response back to the client
    res.status(201).send('User registered successfully');
});

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});