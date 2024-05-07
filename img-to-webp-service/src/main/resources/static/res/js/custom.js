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

<!-- Matomo -->
    var _paq = window._paq = window._paq || [];
    /* tracker methods like "setCustomDimension" should be called before "trackPageView" */
    _paq.push(['disableCookies']);
    _paq.push(['enableHeartBeatTimer']);
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function() {
    var u="https://yikes.phip1611.de/";
    // Rewritten to matomo.php on server.
    _paq.push(['setTrackerUrl', u+'yikes.php']);
    _paq.push(['setSiteId', '11']);
    var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
    // Rewritten to matomo.js on server.
    g.async=true; g.src=u+'yikes.js'; s.parentNode.insertBefore(g,s);
})();
<!-- End Matomo Code -->
