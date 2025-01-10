/**
 * 
 */
const fileInput = document.getElementById('example');

const handleFileSelect = () => {
	const files = fileInput.files;
	for (let i = 0; i < files.length; i++){
		console.log(files[i]);
	}
}

fileInput.addEventListener('change', handleFileSelect);