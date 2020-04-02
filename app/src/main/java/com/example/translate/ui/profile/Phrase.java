package com.example.translate.ui.profile;

public class Phrase {
    private String phraseEn;
    private String phraseCn;
    private String phrasePinyin;

    public Phrase(String phraseEn, String phraseCn, String phrasePinyin) {
        this.phraseEn = phraseEn;
        this.phraseCn = phraseCn;
        this.phrasePinyin = phrasePinyin;
    }

    public String getPhraseEn() {
        return phraseEn;
    }

    public void setPhraseEn(String phraseEn) {
        this.phraseEn = phraseEn;
    }

    public String getPhraseCn() {
        return phraseCn;
    }

    public void setPhraseCn(String phraseCn) {
        this.phraseCn = phraseCn;
    }

    public String getPhrasePinyin() {
        return phrasePinyin;
    }

    public void setPhrasePinyin(String phrasePinyin) {
        this.phrasePinyin = phrasePinyin;
    }
}
