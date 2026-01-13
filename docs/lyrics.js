import { initWinOverlay, openWinOverlay } from "./overlay_handler.js";
import { isAlphaNumeric, Song } from "./song.js";

const ROW_LENGTH = 17;

// Collect song lyrics
const base = "https://raw.githubusercontent.com/corinaminer/lyrics-quiz/master/rvlyrics/";
const song_names = await fetch(base + "manifest.json").then(r => r.json());
const songs = [];
for (const name of song_names) {
    const lyrics = await fetch(base + name + "/lyrics.txt").then(r => r.text());
    songs.push(new Song(name, lyrics));
}

initWinOverlay(document);

const songSelector = document.getElementById("songSelector");
const resetButton = document.getElementById("resetButton");
const peekButton = document.getElementById("peekButton");
const input = document.getElementById("input");
const lyricsTable = document.getElementById("lyricsTable");

function initTableForSong(song) {
    // Set up table for the given song
    lyricsTable.innerHTML = "";
    const lyricsCells = [];
    const rowCount = Math.ceil(song.lyrics.length / ROW_LENGTH);
    const rows = [];
    for (let i=0; i < rowCount; i++) {
        rows.push(lyricsTable.insertRow(-1));
    }
    for (let i=0; i < song.lyrics.length; i++) {
        const row = rows[i % rowCount];
        const col = Math.floor(i / rowCount);
        const cell = row.insertCell(col);
        cell.classList.add("hidden-cell");
        cell.innerHTML = song.lyrics[i];
        lyricsCells.push(cell);
    }
    return lyricsCells;
}

let guessListener;
let lyricCells;
let hasPeeked = false;

function togglePeek(peek) {
    // Toggles the peek button to "Peek" or "Hide". Sets its onclick to toggle the other way.
    const oldClass = peek ? "hidden-cell" : "missed-cell";
    const newClass = peek ? "missed-cell" : "hidden-cell";
    peekButton.innerHTML = peek ? "Peek" : "Hide";
    peekButton.onclick = () => {
        for (const c of lyricCells) {
            if (c.classList.contains(oldClass)) {
                c.classList.remove(oldClass);
                c.classList.add(newClass);
            }
        }
        togglePeek(!peek);
        input.disabled = peek;
        if (!peek) {
            input.focus();
        }
        hasPeeked = true;
    }
}

function play(song) {
    lyricCells = initTableForSong(song);
    let keys_guessed = new Set();

    // Set up input field to check guesses
    guessListener = function(event) {
        const rawGuess = event.target.value.trim().toLowerCase();
        let word = "";
        for (const l of rawGuess) {
            if (isAlphaNumeric(l)) {
                word += l;
            }
        }
        if (!word || keys_guessed.has(word)) {
            return;
        }
        const wordIndices = song.keys.get(word);
        if (wordIndices) {
            keys_guessed.add(word);
            for (const i of (wordIndices || [])) {
                lyricCells[i].classList.remove("hidden-cell");
            }
            input.value = "";
            if (keys_guessed.size == song.keys.size) {
                openWinOverlay(hasPeeked);
            }
        }
    };
    input.addEventListener("input", guessListener);

    // Reset button should revert to no guesses
    resetButton.onclick = () => {
        for (const c of lyricCells) {
            c.classList.add("hidden-cell");
            c.classList.remove("missed-cell");
        }
        keys_guessed = new Set();
        hasPeeked = false;
        togglePeek(true);
        input.disabled = false;
        input.focus();
    }

    hasPeeked = false;
    togglePeek(true);
    input.disabled = false;
    input.focus();
}

function playRandomSong() {
    const randomSongIndex = Math.floor(Math.random() * songs.length);
    play(songs[randomSongIndex]);
}

// Set up songs dropdown to switch songs
const randomSongOptionName = "Random song!"
songSelector.add(new Option(randomSongOptionName));
for (const song of songs) {
    songSelector.add(new Option(song.name));
}
songSelector.addEventListener("change", function(event) {
    input.removeEventListener("input", guessListener);
    if (event.target.value == randomSongOptionName) {
        playRandomSong();
    } else {
        const selectedSong = songs.find(s => s.name == event.target.value);
        play(selectedSong);
    }
})

playRandomSong();
