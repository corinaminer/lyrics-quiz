import { initWinOverlay, openWinOverlay } from "./overlay_handler.js";
import { Song } from "./song.js";

// initOverlays(document);

const ROW_LENGTH = 15;

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
const giveUpButton = document.getElementById("giveUpButton");
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

function play(song) {
    const lyricCells = initTableForSong(song);
    let keys_guessed = new Set();

    // Set up input field to check guesses
    guessListener = function(event) {
        const word = event.target.value.trim().toLowerCase();
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
                openWinOverlay();
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
        input.disabled = false;
        input.focus();
    }

    // Give up button should reveal remaining lyrics
    giveUpButton.onclick = () => {
        for (const c of lyricCells) {
            if (c.classList.contains("hidden-cell")) {
                c.classList.remove("hidden-cell");
                c.classList.add("missed-cell");
            }
        }
        input.disabled = true;
    }

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
