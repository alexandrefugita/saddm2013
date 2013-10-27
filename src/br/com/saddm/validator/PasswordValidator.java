package br.com.saddm.validator;


public class PasswordValidator{

	private String 	message;
	private String password;

	public PasswordValidator(String password) {
		this.password = password;
	}
	
	//Valida a senha
	public boolean isPassOk() {
		if(!isLengthRight(password) || !isPatternRight(password)) {
			this.message = "Senha não atende o padrão de senhas deste sistema";
			return false;
		}
		return true;
	}

	//Verifica se o comprimento da senha atende ao padrão do sistema
	private boolean isLengthRight(String password) {
		if (10 > password.length() || password.length() > 25) {
			return false;
		}
		return true;
	}
	//Verifica se a senha atende ao padrão do sistema
	//Pelo menos uma letra maiuscula
	//Pelo menos um caracter especial
	//Pelo menos um numero;
	private boolean isPatternRight(String password) {
		boolean isCaractererEspecial, isNumero, isLetraMaiuscula;
		int valorASCII;

		isCaractererEspecial = false;
		isNumero = false;
		isLetraMaiuscula = false;

		for (int i = 0; i < password.length(); i++) {
			valorASCII = (int) password.charAt(i);
			if (!isNumero && (48 <= valorASCII && valorASCII <= 57)) {
				isNumero = true;
			}
			
			if (!isLetraMaiuscula && (65 <= valorASCII && valorASCII <= 90)) {
				isLetraMaiuscula = true;
			}
			if (!isCaractererEspecial
					&& ((33 <= valorASCII && valorASCII <= 47)
							|| (58 <= valorASCII && valorASCII <= 64)
							|| (91 <= valorASCII && valorASCII <= 96) 
							|| (123 <= valorASCII && valorASCII <= 126))) {
				isCaractererEspecial = true;
			}
		}
		if(isNumero && isCaractererEspecial && isLetraMaiuscula) {
			return true;
		}
		return false;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
