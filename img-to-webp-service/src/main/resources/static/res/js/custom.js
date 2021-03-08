window.addEventListener('load', () => {
    const inputEle = document.querySelector('#quality-range');


    // can be understood as: we are on upload/convert page
    if (!inputEle) return;


    const indicatorEle = document.querySelector('#input-quality-visualizer');
    inputEle.addEventListener('input', (event) => {
        indicatorEle.innerHTML = event.target.value;
    });

    const consentCheckmarkEle = document.querySelector('#consentCheckmark');
    const uploadButtonEle = document.querySelector('#uploadButton');
    const fileSelectEle = document.querySelector('#uploadButton');

    // default disabled
    uploadButtonEle.disabled = true;
    consentCheckmarkEle.addEventListener('input', (event) => {
        uploadButtonEle.disabled = !event.target.checked;
    });
});
