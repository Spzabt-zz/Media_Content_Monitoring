// Get all elements with class="tabcontent" and hide them
let tabcontents = document.getElementsByClassName("tabcontent");
for (let i = 0; i < tabcontents.length; i++) {
    tabcontents[i].classList.remove("show");
}

// Get all elements with class="tablinks" and add the "active" class to the first button
let tablinks = document.getElementsByClassName("tablinks");
tablinks[0].classList.add("active");
tabcontents[0].classList.add("show");

// Define function to open the clicked tab and hide the others
function openTab(event, tabName) {
    // Hide all tab contents
    for (let i = 0; i < tabcontents.length; i++) {
        tabcontents[i].classList.remove("show");
    }

    // Remove "active" class from all tab links
    for (let i = 0; i < tablinks.length; i++) {
        tablinks[i].classList.remove("active");
    }

    // Show the clicked tab content
    document.getElementById(tabName).classList.add("show");

    // Add the "active" class to the clicked tab link
    event.currentTarget.classList.add("active");
}