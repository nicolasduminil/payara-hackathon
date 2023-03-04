# CHANGELOG File for the Payara Hackathon Application

The 2nd of March 2023 at 11:00 AM CET: the hackathon starts

## Task
Three themes have been proposed and I choose to implement the bank system application.

## The 2nd of March 2023 at 10:30 PM CET
Have implemented 1st version of components: bank-jaxb, bank-jpa, bank-mapping.

## The 3rd of March 2023 at 11:15 AM CET
Have implemented the 1st version of components: bank-batch and bank-infra.

## The 3rd of March 2023 at 12:45 AM CET
Have implemented bank-facade, bank-jms and bank-jaxrs.
Testing now ...

## The 3rd of March 2023 at 9:30 PM CET
Have added unit and integration tests for the components bank-mapping, bank-facade, bank-jaxb, bank-jaxrs.
Have also tried during hours to add support for MP-JWT propagation to bank-jaxrs, but in vain.
It simply doesn't work !
Removed.

## The 4th of March 2023 at 6:00 PM CET
Today I finished to unit and integration testing the whole application, end to end. It works as expected and I didn't 
notice any particular bug. The only thing which gets on my nerves is that trying to log with SLF4J in Payara Server raises 
exceptions. So, I used System.out to do it. Works as expected in Payar Micro. I didn't have the time to look at it but 
it's a minor thing.

I still regret that I wasn't able to get the JWT running. I'm using currently this Eclipse MP feature on platforms like
Wildfly, Quarkus and OpenLiberty, but this time, on Payara Server, it simply didn't work. Any relation with the secure admin ?
Unfortunatelly, I run out of time and I couldn't spend more on this, but never mind, I'll do it some other time.

I've been thinking at long wether to provide or not a GUI. I didn't because my application is a batch processing one and,
as such, it doesn't require it. It wasn't anyway a condition in the Hackathon rules, only optional. And I think that most 
of those who will provide one will be in JavaScript or TypeScript, which isn't relevant for neither Jakarta EE, nore for
Eclipse MP. I would have added a simple GUI to the application if I had time. But it turns out that it's not so simple as
my data model is quite complex and tricky. And I wasted too much time with the JWT stuff, so I'm running out of time.

Unfortunatelly, I'm not available to work on that tomorrow and Monday, the 6th of March, so I'm adding now the last 
final touch to the project. The time frame of the Hackathon wasn't the best for me this time, however I wanted to be a 
part of it 'cause it was fun. What a great idea the Payara guys had to organize it ! If even a *silver* developer like 
myself, who doesn't have anything to prove, was interested to participate, then it means that is was really interesting.

