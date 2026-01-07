function isAlphaNumeric(char) {
  return /^[a-z0-9]$/i.test(char);
}

export class Song {
    constructor(name, lyrics) {
        this.name = name;
        const initial_lyrics = lyrics.trim().split(/\s+/);
        this.lyrics = [];
        this.keys = new Map();
        for (let i = 0; i < initial_lyrics.length; i++) {
            let key = "";
            for (const l of initial_lyrics[i])
                if (isAlphaNumeric(l)) {
                    key += l.toLowerCase();
                }
            if (key.length) {
                this.lyrics.push(initial_lyrics[i]);
                this.keys.has(key) ? this.keys.get(key).push(i) : this.keys.set(key, [i]);
            }
        }
    }
}
