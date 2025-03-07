window.onload = function () {
    loadDashboard();
};
function openSidebar() {
    document.getElementById("mySidebar").style.width = "250px";
    document.querySelector(".main-content").style.marginLeft = "250px";
    document.getElementById("overlay").style.display = "block"; // Show overlay
}

function closeSidebar() {
    document.getElementById("mySidebar").style.width = "0";
    document.querySelector(".main-content").style.marginLeft = "0";
    document.getElementById("overlay").style.display = "none"; // Hide overlay
}

function loadDashboard() {
    // Retrieve user ID, and token from local storage
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');

    fetchUserData(userId, token);
    fetchTransfersData(userId, token)
}

function fetchUserData(userId, token) {
    fetch(`http://localhost:8080/api/v1/user/${userId}`, {
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
        document.getElementById('account-name-header').innerText = data.firstName;
        document.getElementById('account-balance').innerText = data.accounts[0].balance;
    })
    .catch(error => {
        console.error('Error fetching user data:', error);
    });
}

function fetchTransfersData(userId, token) {
    fetch(`http://localhost:8080/api/v1/transfer/${userId}`, {
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