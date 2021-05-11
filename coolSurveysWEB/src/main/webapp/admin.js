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
        addQuestion(questions, index) {
            questions.push({ question: "" });
            this.options.push([{ option: "" }]);
            this.multipleChoice = false;
            this.questionAdded[index] = true;
        },
        setOpenAnswer(questionIndex, options) {
            if (options[questionIndex]!==null && options[questionIndex][0].option !== "")
                options.splice(questionIndex, 1);
            this.multipleChoice = false;
        },
        removeQuestion(index, questions) {
            questions.splice(index, 1);
            this.questionAdded[index] = false;
        },
        addOption(options, optionIndex) {
            if (options[optionIndex].option !== "")
                options.push({ option: "" });
        },
        setMultipleChoice(questionIndex) {
            this.multipleChoice = true;
        },
        removeOption(index, options) {
            options.splice(index, 1);
        },
        submitQuestionsForm(){
            let toSend = new Map();
            let i;
            if (this.questions.length === this.options.length) {
                //returned a map of questions (keys) -> array of options (value)
                for(i=0; i < this.questions.length; i++) {
                    toSend.set(this.questions[i], this.options[i]);
                }
                const object = Object.fromEntries(toSend);
                axios.post("./AdminSurvey", {
                    object
                }).then(response => {
                    this.leaderboard = true;
                    this.section2 = false;
                    console.log(response.data)

                }).catch(response => {
                    console.log(response.data)
                });
            }
        }
    },
});