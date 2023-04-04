package com.slokam.da.hc.service.impl;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.slokam.da.hc.dao.AppointmentDAO;
import com.slokam.da.hc.dao.IDoctorDAO;
import com.slokam.da.hc.dao.PatientDAO;
import com.slokam.da.hc.dao.VisitingDAO;
import com.slokam.da.hc.dto.VisitingDto;
import com.slokam.da.hc.entity.Appointment;
import com.slokam.da.hc.entity.Doctor;
import com.slokam.da.hc.entity.Patient;
import com.slokam.da.hc.entity.Visiting;
import com.slokam.da.hc.exception.PatientException;
import com.slokam.da.hc.service.IVisitingService;

@Service
public class VisitingServiceImpl implements IVisitingService{

	private static Logger LOOGER = LoggerFactory.getLogger(VisitingServiceImpl.class);
	@Autowired
	private VisitingDAO visitingDao;
	
	@Autowired
	private PatientDAO patientDao;
	
	@Autowired
	private IDoctorDAO doctorDao;
	@Autowired
	private AppointmentDAO appointmentDAO;
	@Override
	public void visit(Visiting visiting) throws PatientException {
		LOOGER.debug("Entered into visit method.");
		try {
			if(visiting!=null){
				LOOGER.debug("visiting details::"+visiting);
				visiting.setTime(new Date());
			  visitingDao.save(visiting);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PatientException("DB error", e);
		}
		LOOGER.debug("Exit from visit method.");
	}
	@Transactional
	public void saveVisiting(VisitingDto visitingDto) {
		 Patient patient = new Patient();
		 patient.setName(visitingDto.getName());
		 patient.setDob(visitingDto.getDob());
		 patient.setPhone(visitingDto.getPhone());
		
		 patientDao.save(patient);
		 // savePatient();
		 
		 Appointment appointment = new Appointment();
		 appointment.setTaken(new Date());
		 appointment.setReqDate(visitingDto.getReqDate());
		 appointment.setPatient(patient);
		 appointmentDAO.save(appointment);
		 // saveAppointment();
		 
		 Optional<Doctor> doctorOpt =  doctorDao.findById(visitingDto.getDoctorId());
		 
		 Visiting visiting =new Visiting();
		 visiting.setDoctor(doctorOpt.get());
		  
		 visiting.setAppointment(appointment);
		 visiting.setTime(new Date());
		 
		 visitingDao.save(visiting);
		 // saveVistiing();
	}
	
	
}
