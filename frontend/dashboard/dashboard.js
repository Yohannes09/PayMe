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
    fetchUserData();
    fetchTransfersData();
}


function fetchUserData() {
    
    fetch('/api/v1/PayMe/user') 
        .then(response => response.json())
        .then(data => {
            document.getElementById('account-name').innerText = data.name;
            document.getElementById('account-balance').innerText = data.balance;
            document.getElementById('account-name-header').innerText = data.name;
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
        });
}


function fetchTransfersData() {
    
    fetch('/api/v1/PayMe/transfers') 
        .then(response => response.json())
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
window.onload = function() {
    loadDashboard();
};