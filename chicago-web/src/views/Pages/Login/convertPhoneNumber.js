const convertPhoneNumber=(inputStr) =>
{
  let outStr;

  console.log('start convertPhoneNumber=', inputStr);

  outStr =  inputStr.replace(/\s/g, '')
  outStr=outStr.replace(/\(/g, '')
  outStr=outStr.replace(/\)/g, '')
  outStr =  outStr.replace(/^8/, '')
  outStr=outStr.replace(/^\+7/, '')
  console.log('convertPhoneNumber=', inputStr, outStr);
  
  return outStr;
}
export default convertPhoneNumber;