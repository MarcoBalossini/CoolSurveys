package it.polimi.db2.coolSurveysWEB.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResponseQuestionnaire {

    public static class ResponseQuestion {

        private int number;
        private String question;
        private List<ResponseOption> options = new ArrayList<>();
        int section;
        String type;

        public ResponseQuestion(int number, String question, int section) {
            this.number = number;
            this.question = question;
            this.section = section;
        }

        public ResponseQuestion(int number, String question, int section, String type) {
            this.number = number;
            this.question = question;
            this.section = section;
            this.type = type;
        }

        public void setOptions(List<ResponseOption> options) {
            this.options = options;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getQuestion() {
            return question;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResponseQuestion question1 = (ResponseQuestion) o;
            return number == question1.number && Objects.equals(question, question1.question) && Objects.equals(options, question1.options);
        }
    }

    public static class ResponseOption {

        private int optionNumber;
        private final String text;

        public ResponseOption(int optionNumber, String text) {
            this.optionNumber = optionNumber;
            this.text = text;
        }

        public void setNumber(int number) {
            this.optionNumber = number;
        }

        public int getNumber() {
            return optionNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResponseOption option = (ResponseOption) o;
            return optionNumber == option.optionNumber && Objects.equals(text, option.text);
        }
    }

    private String name;
    private LocalDate date;
    private byte[] photo;
    private List<ResponseQuestion> questions = new ArrayList<>();

    public ResponseQuestionnaire(String name, byte[] photo, LocalDate date) {
        this.name = name;
        this.photo = photo;
        this.date = date;
    }

    public void setQuestions(List<ResponseQuestion> questions) {
        this.questions = questions;
    }

    public void addQuestions(List<ResponseQuestion> questions) {
        List<ResponseQuestion> list = new ArrayList<>();
        list.addAll(this.questions);
        list.addAll(questions);
        this.questions = list;
    }

    public int getNumberOfQuestions() {
        return questions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseQuestionnaire that = (ResponseQuestionnaire) o;
        return Objects.equals(name, that.name) && Objects.equals(date, that.date) && Arrays.equals(photo, that.photo) && Objects.equals(questions, that.questions);
    }
}