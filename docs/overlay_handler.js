let overlayBackgroundDiv;
let overlayDiv;
let overlayTextDiv;

function closeOverlay() {
    overlayDiv.classList.remove("active");
    overlayBackgroundDiv.classList.remove("active");
}

export function openWinOverlay(hasPeeked) {
    if (hasPeeked) {
        overlayTextDiv.innerHTML = `
            Nice work!
            <div style="font-size: 22px">Next time, try without peeking 😉</div>
        `;
    } else {
        overlayTextDiv.innerHTML = "Congratulations, you got it!!!";
    }
    overlayDiv.classList.add("active");
    overlayBackgroundDiv.classList.add("active");
}

export function initWinOverlay(document) {
    overlayBackgroundDiv = document.querySelector(".overlay-bkgd");
    overlayDiv = document.querySelector(".overlay");
    overlayTextDiv = document.getElementById("overlayText");

    // Remove overlay when user clicks "x" icon or page outside overlay, or hits escape
    document.querySelector(".overlay-bkgd").onclick = closeOverlay;
    document.querySelector(".close-btn").onclick = closeOverlay;
    document.addEventListener("keydown", event => {
        if (event.key === "Escape") {
            closeOverlay();
        }
    });
}
