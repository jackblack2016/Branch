package com.prime.question;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.prime.customer.model.Customer;
import com.prime.customer.service.CustomerService;
import com.prime.question.model.Question;
import com.prime.question.service.QuestionService;
import com.prime.question.service.ResponseService;

@Controller
@Scope("session")
public class AnswerQuestionBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger( AnswerQuestionBean.class.getName() );
	
	
	private List<Question> questions;
	
	private int currentQuestionIndex =0;
	
	private String response;
	private Customer customer;
	
	




	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private ResponseService responseService;
	
	@Autowired
	private CustomerService customerService;
	
	
	@PostConstruct
	public void init(){
		setQuestions(questionService.listAll());
		customer = customerService.createNewCustomer();
	}




	public String doNext(){
		
		logger.info("response : " + getResponse());
		Question question = questions.get(currentQuestionIndex);
		responseService.createNewResponse(customer.getCustomerId(),question.getQuestionId(), question.getQuestionBody(), response);
		
		currentQuestionIndex ++;
		//System.out.println("currentQuestionIndex " + currentQuestionIndex);
		if(currentQuestionIndex == questions.size() ){
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			return "ThankYou";
			
		}
		response=null;
		return "AnswerQuestions";
	}
	

	public String doForward(){
		
		if(currentQuestionIndex == 0 ){
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			return "StartToAnswerQuestion";
			
		}
		logger.info("response : " + getResponse());
		Question question = questions.get(currentQuestionIndex);
		responseService.createNewResponse(customer.getCustomerId(),question.getQuestionId(), question.getQuestionBody(), response);
		System.out.println("currentQuestionIndex " + currentQuestionIndex);
		currentQuestionIndex --;
		System.out.println("currentQuestionIndex " + currentQuestionIndex);
		
		response=null;
		return "AnswerQuestions";
	}
	
	
//	public void checkQuestionIndex() throws ValidatorException
//	{
//		logger.info("Cheeck method");
//		if ( 0 == currentQuestionIndex ) 
//		{
//			FacesMessage message = addMessage("This is already the first question , cannot move forward.") ;
//			System.out.println("This is already the first question , cannot move forward.");
//			throw new ValidatorException(message) ; 
//			
//		}
//	}

//	
//	public FacesMessage  addMessage(String summary) {
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
//       // FacesContext.getCurrentInstance().addMessage(null, message);
//        return  message ;
//    }

	
	public Question getCurrentQuestion(){
		return this.getQuestions().get(currentQuestionIndex);
	}

	
	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}




	public List<Question> getQuestions() {
		return questions;
	}




	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}




	public int getCurrentQuestionIndex() {
		return currentQuestionIndex;
	}




	public void setCurrentQuestionIndex(int currentQuestionIndex) {
		this.currentQuestionIndex = currentQuestionIndex;
	}




	public String getResponse() {
		return response;
	}




	public void setResponse(String response) {
		
		System.out.println("response " + response);
		this.response = response;
	}

	
	public Customer getCustomer() {
		return customer;
	}





	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public int getTotalNumberOfQuestions(){
		return questions.size();
	}
}
