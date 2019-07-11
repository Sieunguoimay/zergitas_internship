#include<iostream>
#include<vector>
class Subject{
	int value;
	std::vector<class Observer*> views;
public:
	void attach(Observer*view){
		views.push_back(view);
	}
	void notify();
	void changeValue(int newValue){
		std::cout<<"Hey humans, I'm about to change my value. Watch out for it\n";
		value = newValue;
		notify();
	}
	inline int getValue(){return value;}
};

class Observer{
	Subject*model;
	int denom;
public:
	Observer(Subject*model, int div){
		this->model = model;
		denom = div;
		model->attach(this);
	}
	//do something on notified
	virtual void update()=0;
protected:
	inline Subject*getSubject(){return model;}	
	inline int getDivisor(){return denom;}
};

void Subject::notify(){
	for(auto&a: views)
		a->update();
}

class DivObserver: public Observer{
public:
	DivObserver(Subject*model, int div)
		: Observer(model, div){}
	void update()override{		
		std::cout<<"DIV:Here I can access what inside observer and do something with it\n";
		std::cout<<"DIV: "<<getDivisor()<<'\n';
	}	

};

class ModObserver: public Observer{
public:
	ModObserver(Subject*model, int div)
		: Observer(model, div){}
	void update()override{		
		std::cout<<"MOD:Here I can access what inside observer and do something with it\n";
		std::cout<<"MOD: "<<getDivisor()<<'\n';
	}	
};

int main(){
	std::cout<<"Hello Observer Pattern\n";
	Subject model;

	ModObserver o1(&model, 1);
	DivObserver o2(&model, 2);

	//ready to change
	model.changeValue(12);
	return 0;
}