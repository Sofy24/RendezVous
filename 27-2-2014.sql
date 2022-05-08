create table Persona(
	P_ID int, 
	P_NOME VARCHAR2(20),
	P_COGNOME VARCHAR2(20),
	P_ETA int, 
	primary key(P_ID)
);

create table Relazioni(
	R_P1 int,
	R_P2 int,
	R_tipo VARCHAR2(20),
	primary key(R_P1, R_P2),
	foreign key (R_p1) references Persona(P_ID),
	foreign key (R_p2) references Persona(P_ID)
):


create or replace procedure influencer is 

cursor c_persone is
	select * 
	from Persona;


begin
	for v_persona in c_persone loop
		int punteggio := 0;
		select   
		from  (
			-- amici di 2 livello
			select r.R_P2  as Amici2Livello -- *
			from (
				select p2.* as Amici1Livello 
				from Persona p2 join Relazioni r on (p2.P_ID = r.R_P2)
				where r.R_P1 = v_persona.P_ID;
				
			) join Relazioni r on (Amici1Livello.P_ID = r.R_P1) 
			where r.R_P2 not v_persona.P_ID --escludo relazioni circolari;
	) 
	having count(*) > 100;
	end loop;

end;





-- me ne frego del nome e faccio un group by e bona


create or replace procedure influencer is 

cursor c_persone is
	select * 
	from Persona;

cursor c_getData(PID int)  is 
	select   
			from  (
				-- amici di 2 livello
				select count(*) as Numtipo, r.R_tipo as  TipoLivello2  -- AMici di livello 2
				from (
					select p2.* as Amici1Livello 
					from Persona p2 join Relazioni r on (p2.P_ID = r.R_P2)
					where r.R_P1 = PID;
					
				) join Relazioni r on (Amici1Livello.P_ID = r.R_P1) 
				where r.R_P2 not PID --escludo relazioni circolari;
				group by r.R_Tipo
		) 
		where sum(NumTipo) > 100;

begin
	for v_persona in c_persone loop
		int punteggio := 0;
		for v_data in c_getData(v_persona.P_ID) loop
			if v_data.TipoLivello2 = "Work with" then 
				punteggio := punteggio + (v_data.NumTipo * 1)
			else 
				if v_data.TipoLivello2 = "Friend of" then 
					punteggio := punteggio + (v_data.NumTipo * 0.5)
				end if; -- necessario ??
			end if;
		end loop;
		DBMS_OUTPUT.PUTLINE(v_persona.P_nome || ' ' || v_persona.P_cognome || ' possiede un punteggio di ' || punteggio );  
	end loop;

end;
