package com.example.translate;

public class Phrase {

    private String id;
    private String phraseEn;
    private String phraseCn;
    private String pinyin;
    private String category;
    private String learned;
    private String saved;

    public Phrase(){
    }

    public Phrase(String id, String phraseEn, String phraseCn, String pinyin, String category, String learned, String saved) {
        this.id = id;
        this.phraseEn = phraseEn;
        this.phraseCn = phraseCn;
        this.pinyin = pinyin;
        this.category = category;
        this.learned = learned;
        this.saved = saved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLearned() {
        return learned;
    }

    public void setLearned(String learned) {
        this.learned = learned;
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }
}
