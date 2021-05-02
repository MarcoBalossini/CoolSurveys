let index = new Vue ({
    el: "#admin",
    data: {
        questions: [{ question: "" }],
        options: [[{ option: "" }]],
        multipleChoice: false,
        questionAdded:[],
        message: '',
        surveyCreation: true
    },
    computed: {},
    methods: {
        addQuestion(value, questions, index) {
            questions.push({ value: "" });
            this.multipleChoice = false;
            this.questionAdded[index] = true;
        },
        setOpenAnswer(questionIndex, options) {
            if (options[questionIndex][0].option !== "")
                options.splice(questionIndex, 1);
            this.multipleChoice = false;
        },
        removeQuestion(index, questions) {
            questions.splice(index, 1);
            this.questionAdded[index] = false;
        },
        addOption(value, options, optionIndex) {
            if (options[optionIndex].value !== "" || options[optionIndex].option !== "")
                options.push({ value: "" });
        },
        removeOption(index, options) {
            options.splice(index, 1);
        },
        submitQuestionsForm(){
            //TODO:develop
        }
    },
});