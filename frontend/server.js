const express = require('express');
const cors = require('cors');
const path = require('path');

const app = express();
const PORT = 3000; 

app.use(cors()); // Enable CORS for all requests
app.use(express.static(path.join(__dirname, 'public')))

// Cache control middleware (useful during development)
app.use((req, res, next) => {
    res.setHeader('Cache-Control', 'no-store, no-cache, must-revalidate, private');
    next();
});

// Serve components
app.get('/home', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'home', 'home.html'));
});

app.get('/register', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'authentication', 'register.html'));
});

app.get('/login', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'authentication', 'login.html'));
});

app.get('/dashboard', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'dashboard', 'dashboard.html'));
});


//
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).send('Something broke!');
});

app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});