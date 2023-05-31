let tabcontents = document.getElementsByClassName("tabcontent");
for (let i = 0; i < tabcontents.length; i++) {
    tabcontents[i].classList.remove("show");
}

let tablinks = document.getElementsByClassName("tablinks");
tablinks[0].classList.add("active");
tabcontents[0].classList.add("show");

function openTab(tabName) {
    for (let i = 0; i < tabcontents.length; i++) {
        tabcontents[i].classList.remove("show");
    }

    for (let i = 0; i < tablinks.length; i++) {
        tablinks[i].classList.remove("active");
    }

    document.getElementById(tabName).classList.add("show");

    this.classList.add("active");
}
