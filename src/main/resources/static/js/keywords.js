const keywordsInput = document.getElementById('keywords');

// Initialize the input with any existing keywords


// Listen for changes to the input and update the keywords list
keywordsInput.addEventListener('input', (event) => {
    const newKeywords = event.target.value.split(',').map(keyword => keyword.trim());
    console.log(newKeywords); // Replace with your own logic to update the keywords list
});
