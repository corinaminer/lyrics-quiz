export function isAlphaNumeric(char) {
  return /^[a-z0-9]$/i.test(char);
}

class Lyric {
    constructor(word, parts) {
        this.parts = parts == undefined ? "" : parts;
        this.word = word;
    }

    forPart(part) {
        if (part == "soprano") {
            return this.parts.includes("s");
        } else if (part == "alto") {
            return this.parts.includes("a");
        } else if (part == "tenor") {
            return this.parts.includes("t");
        } else if (part == "bass") {
            return this.parts.includes("b");
        }
    }
}

export class Song {
    constructor(name, lyrics) {
        this.name = name;
        const initial_lyrics = lyrics.trim().split(/\s+/);
        this.lyrics = [];
        this.keys = new Map();
        let currentParts = undefined;
        this.hasParts = false;
        for (const word of initial_lyrics) {
            if (word[0] == "[" && word[word.length - 1] == "]") {
                // parts denotation
                currentParts = word;
                this.hasParts = true;
                continue;
            }
            // Determine the key for this word
            let key = "";
            for (const l of word)
                if (isAlphaNumeric(l)) {
                    key += l.toLowerCase();
                }
            if (key.length) {
                const wordIndex = this.lyrics.length;
                this.lyrics.push(new Lyric(word, currentParts));
                this.keys.has(key) ? this.keys.get(key).push(wordIndex) : this.keys.set(key, [wordIndex]);
            }
        }
    }
}
