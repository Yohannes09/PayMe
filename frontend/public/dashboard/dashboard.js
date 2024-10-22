/* Function to open the sidebar */
function openNav() {
    document.getElementById("mySidebar").style.width = "250px";
    document.querySelector(".main-content").style.marginLeft = "250px";
    document.getElementById("overlay").style.display = "block"; // Show overlay
}

/* Function to close the sidebar */
function closeNav() {
    document.getElementById("mySidebar").style.width = "0";
    document.querySelector(".main-content").style.marginLeft = "0";
    document.getElementById("overlay").style.display = "none"; // Hide overlay
}

/* Function to load dashboard data dynamically */
function loadDashboard() {
    const userId = localStorage.getItem('userId'); // Retrieve user ID from local storage
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    console.log(userId);
    console.log(token);
    fetchUserData(userId, token);
}

function fetchUserData(userId, token) {
    fetch(`http://localhost:8080/api/v1/payme/user/${userId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
        document.getElementById('account-name').innerText = `${data.firstName} ${data.lastName}`;
        //document.getElementById('account-balance').innerText = data.accounts[0].balance;
        document.getElementById('account-name-header').innerText = data.firstName;
    })
    .catch(error => {
        console.error('Error fetching user data:', error);
    });
}

function fetchTransfersData(token) {
    fetch('http://localhost:8080/api/v1/PayMe/transfers', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        const transfersList = document.getElementById('transfers-list');
        transfersList.innerHTML = ''; // Clear existing list

        data.transfers.forEach(transfer => {
            const li = document.createElement('li');
            li.innerText = transfer;
            transfersList.appendChild(li);
        });
    })
    .catch(error => {
        console.error('Error fetching transfers data:', error);
    });
}

/* Trigger loading data when the page is ready */
document.addEventListener('DOMContentLoaded', function() {
    loadDashboard();
});