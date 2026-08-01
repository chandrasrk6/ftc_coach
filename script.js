document.addEventListener('DOMContentLoaded', () => {

  // --- 1. Tab Navigation ---
  const navBtns = document.querySelectorAll('.nav-btn');
  const tabContents = document.querySelectorAll('.tab-content');

  window.switchTab = function(tabId) {
    navBtns.forEach(btn => {
      if (btn.dataset.tab === tabId) {
        btn.classList.add('active');
      } else {
        btn.classList.remove('active');
      }
    });

    tabContents.forEach(content => {
      if (content.id === tabId) {
        content.classList.add('active');
      } else {
        content.classList.remove('active');
      }
    });
  };

  navBtns.forEach(btn => {
    btn.addEventListener('click', () => switchTab(btn.dataset.tab));
  });


  // --- 2. Interactive Limelight Simulator Widget ---
  const sliderTx = document.getElementById('slider-tx');
  const sliderTa = document.getElementById('slider-ta');
  const checkTargetValid = document.getElementById('check-target-valid');
  const checkDeadman = document.getElementById('check-deadman');

  const simTarget = document.getElementById('sim-target');
  const valTx = document.getElementById('val-tx');
  const valTa = document.getElementById('val-ta');
  const valState = document.getElementById('val-state');
  const valDrive = document.getElementById('val-drive');
  const valTurn = document.getElementById('val-turn');
  const valLeft = document.getElementById('val-left');
  const valRight = document.getElementById('val-right');

  const KP_TURN = 0.025;
  const KP_DRIVE = 0.035;
  const TARGET_AREA_DESIRED = 8.0;
  const MIN_SAFETY_AREA = 18.0;
  const MAX_SPEED = 0.4;

  function updateSimulator() {
    const tx = parseFloat(sliderTx.value);
    const ta = parseFloat(sliderTa.value);
    const isValid = checkTargetValid.checked;
    const deadmanPressed = checkDeadman.checked;

    valTx.textContent = `${tx > 0 ? '+' : ''}${tx.toFixed(1)}°`;
    valTa.textContent = `${ta.toFixed(1)}%`;

    // Position target visually in screen container
    // tx ranges from -31 (left: 10%) to +31 (left: 90%)
    const leftPercent = 50 + (tx / 31) * 40;
    // ta ranges from 0.5% (size 25px) to 25% (size 110px)
    const targetSize = Math.max(25, Math.min(120, 25 + (ta / 25) * 85));

    if (isValid) {
      simTarget.style.display = 'flex';
      simTarget.style.left = `calc(${leftPercent}% - ${targetSize / 2}px)`;
      simTarget.style.width = `${targetSize}px`;
      simTarget.style.height = `${targetSize}px`;
    } else {
      simTarget.style.display = 'none';
    }

    // Safety & Motor Calculation Logic
    if (!deadmanPressed) {
      valState.textContent = 'STANDBY (Trigger Released)';
      valState.style.color = '#ffb703';
      setOutputs(0, 0, 0, 0);
      return;
    }

    if (!isValid) {
      valState.textContent = 'SEARCHING (No Target)';
      valState.style.color = '#ef476f';
      setOutputs(0, 0, 0, 0);
      return;
    }

    // P-Control Calculations
    let turnPower = tx * KP_TURN;
    let drivePower = (TARGET_AREA_DESIRED - ta) * KP_DRIVE;

    // Safety Check: Emergency Halt if target is too close
    if (ta > MIN_SAFETY_AREA) {
      drivePower = Math.min(drivePower, 0);
      valState.textContent = '⚠️ SAFETY HALT (Too Close!)';
      valState.style.color = '#ef476f';
    } else {
      valState.textContent = 'ACTIVE TRACKING';
      valState.style.color = '#06d6a0';
    }

    // Clamping to max speed limit
    drivePower = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, drivePower));
    turnPower  = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, turnPower));

    // Arcade Drive Mixing & Normalization
    let leftMotor = drivePower + turnPower;
    let rightMotor = drivePower - turnPower;

    const maxMag = Math.max(Math.abs(leftMotor), Math.abs(rightMotor));
    if (maxMag > 1.0) {
      leftMotor /= maxMag;
      rightMotor /= maxMag;
    }

    setOutputs(drivePower, turnPower, leftMotor, rightMotor);
  }

  function setOutputs(drive, turn, left, right) {
    valDrive.textContent = `${drive >= 0 ? '+' : ''}${drive.toFixed(2)}`;
    valTurn.textContent = `${turn >= 0 ? '+' : ''}${turn.toFixed(2)}`;
    valLeft.textContent = `${left >= 0 ? '+' : ''}${left.toFixed(2)}`;
    valRight.textContent = `${right >= 0 ? '+' : ''}${right.toFixed(2)}`;
  }

  if (sliderTx && sliderTa) {
    sliderTx.addEventListener('input', updateSimulator);
    sliderTa.addEventListener('input', updateSimulator);
    checkTargetValid.addEventListener('change', updateSimulator);
    checkDeadman.addEventListener('change', updateSimulator);
    updateSimulator();
  }


  // --- 3. Accordion Toggles ---
  document.querySelectorAll('.accordion-header').forEach(header => {
    header.addEventListener('click', () => {
      const body = header.nextElementSibling;
      body.classList.toggle('open');
      const arrow = header.querySelector('.arrow');
      if (arrow) {
        arrow.textContent = body.classList.contains('open') ? '▲' : '▼';
      }
    });
  });

});
