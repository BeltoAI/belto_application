function toggleMenu() {
    var menu = document.getElementById("dropdownMenu");
    var content = document.getElementById("mainContent");

    if (menu.style.left === "0px") {
        menu.style.left = "-250px"; 
        content.style.marginLeft = "0"; 
    } else {
        menu.style.left = "0px"; 
        content.style.marginLeft = "250px"; 
    }
}


function addToCart(nftId, price) {
    let cart = localStorage.getItem('cart') ? JSON.parse(localStorage.getItem('cart')) : [];


    const existingItemIndex = cart.findIndex(item => item.nftId === nftId);
    if (existingItemIndex !== -1) {
        cart[existingItemIndex].quantity += 1;
    } else {
        cart.push({ nftId: nftId, price: price, quantity: 1 });
    }

    localStorage.setItem('cart', JSON.stringify(cart));


    showPopup("Successfully added to shopping cart");
}


function showPopup(message) {
    const popup = document.getElementById("popup-message");
    popup.textContent = message;
    popup.style.display = "block";


    setTimeout(() => {
        popup.style.display = "none";
    }, 3000);
}

function showBio(bioId) {
    document.getElementById(bioId).style.display = 'block';
}

function hideBio(bioId) {
    document.getElementById(bioId).style.display = 'none';
}


function toggleAnswer(answerId) {
    var answer = document.getElementById(answerId);
    var arrow = document.getElementById('arrow' + answerId.substring(6));

    if (answer.classList.contains('open')) {
        answer.classList.remove('open');
        arrow.innerHTML = "&#9660;"; 
        answer.style.maxHeight = '0px'; 
    } else {
        answer.classList.add('open');
        arrow.innerHTML = "&#9650;"; 
        answer.style.maxHeight = answer.scrollHeight + "px"; 
    }
}